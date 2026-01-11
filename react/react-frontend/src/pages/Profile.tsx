import { useProfileFeed } from "../hooks/useProfileFeed";
import { useProfile } from "../hooks/useProfile";
import styles from "./Profile.module.css"
import { useAuth } from "../context/AuthContext.tsx";
import { useEffect } from "react";
import { useScreen } from "../context/ScreenContext.tsx";
import PostItem from "../components/Posts/PostItem.tsx";
import { useEditDeletePost } from "../hooks/useEditDeletePost.ts";

export default function Profile() {

    const { setScreen } = useScreen();
    const { profile, loading: loadingProfile } = useProfile();
    const { posts, setPosts, loading: loadingPosts, fetchPosts, page, hasMore } = useProfileFeed();
    const { editPost, deletePost } = useEditDeletePost(setPosts);
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

                    {profile.profileImageUrl && (
                        <img src={profile.profileImageUrl} alt="Profile picture" />
                    )}
                    <h2>{profile.username}</h2>

                    <button onClick={() => setScreen("createPost")}>Create Post</button>
                    <button onClick={() => setScreen("editUser")}>Edit User</button>
                </div>

                <ul className={styles.profileFeedList}>
                    {loadingPosts && page === 0 ? (
                        <p>Loading Posts... </p>
                    ) : posts.length === 0 ? (
                        <p>No posts yet</p>
                    ) : (
                        <>
                            {posts.map(post => (
                                <PostItem
                                    key={post.id}
                                    post={post}
                                    onEdit={editPost}
                                    onDelete={deletePost}
                                />
                            ))}
                        </>
                    )}
                </ul>
                {hasMore && !loadingPosts && (
                    <button onClick={() => fetchPosts(page + 1)}>Load more</button>
                )}

                {loadingPosts && page > 0 && <p>Loading more posts... </p>}
            </div>

        </>
    )
}