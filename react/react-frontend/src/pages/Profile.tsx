import { Screen } from "../App";
import { useProfileFeed } from "../hooks/useProfileFeed";
import { useProfile } from "../hooks/useProfile";

export default function Profile({ setScreen }: { setScreen: (s: Screen) => void }) {

    const { profile, loading: loadingProfile } = useProfile();
    const { posts, loading: loadingPosts, fetchPosts, page, hasMore } = useProfileFeed();

    if (loadingProfile) return <p>Loading profile... </p>
    if (!profile) return <p>Profile not found</p>

    return (
        <>
        <div>
            <h2>{profile.username}</h2>
            <p>{profile.email}</p>
            <p>{profile.roles.join(", ")}</p>

            <h3>Your posts</h3>
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