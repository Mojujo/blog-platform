import { useProfileFeed } from "../hooks/useProfileFeed";
import { useProfile } from "../hooks/useProfile";
import styles from "./Profile.module.css"
import { ChangeUsernameForm } from "../components/UserSettings/ChangeUsernameForm";
import { ChangeEmailForm } from "../components/UserSettings/ChangeEmailForm";
import { ChangePasswordForm } from "../components/UserSettings/ChangePasswordForm.tsx";
import { useAuth } from "../context/AuthContext.tsx";
import { useEffect } from "react";
import { useScreen } from "../context/ScreenContext.tsx";

export default function Profile() {

    const { setScreen } = useScreen(); 
    const { profile, loading: loadingProfile } = useProfile();
    const { posts, loading: loadingPosts, fetchPosts, page, hasMore } = useProfileFeed();
    const { user, authLoaded } = useAuth();

    useEffect(() => {
        if (authLoaded && !user) {
            setScreen("login")
        }
    }, [authLoaded, user, setScreen])

    if (loadingProfile) return <p>Loading profile... </p>
    if (!profile) return <p>Profile not found</p>

    return (
        <>
            <div className={styles.profileContainer}>
                <div className={styles.profile}>
                    <h2>{profile.username}</h2>
                    <p>{profile.email}</p>
                    <p>{profile.roles.join(", ")}</p>

                    <h3>Your posts</h3>

                    <button onClick={() => setScreen("createPost")}>Create Post</button>
                </div>
                <div>
                    <ChangeUsernameForm />
                    <ChangeEmailForm />
                    <ChangePasswordForm />
                </div>

                {loadingPosts && page === 0 ? (
                    <p>Loading posts... </p>
                ) : posts.length === 0 ? (
                    <p>No posts yet</p>
                ) : (
                    <ul>
                        {posts.map(post => (
                            <li key={post.id}>
                                <h4>{post.title}</h4>
                                <p>{post.content}</p>
                                <small>
                                    {new Date(post.createdDate).toLocaleString()}
                                </small>
                            </li>
                        ))}
                    </ul>
                )}

                {hasMore && !loadingPosts && (
                    <button onClick={() => fetchPosts(page + 1)}>Load more</button>
                )}

                {loadingPosts && page > 0 && <p>Loading more posts... </p>}
            </div>
        </>
    )
}