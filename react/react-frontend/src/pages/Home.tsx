import PostItem from "../components/Posts/PostItem";
import { useFeed } from "../hooks/useFeed";
import { useEditDeletePost } from "../hooks/useEditDeletePost";
import styles from "./Home.module.css";

export default function Home() {
    const { posts, setPosts, loading, fetchPosts, page, hasMore } = useFeed();
    const { editPost, deletePost } = useEditDeletePost(setPosts);

    return (
        <>
            <div className={styles.feedContainer}>
                <h1>Recent Posts</h1>

                {posts.length === 0 && !loading && <p>No posts yet</p>}

                <ul className={styles.feedList}>
                    {posts.map(post => (
                        <PostItem
                            key={post.id}
                            post={post}
                            onEdit={editPost}
                            onDelete={deletePost}
                            showAuthor={true}
                        />
                    ))}
                </ul>

                {loading && <p>Loading... </p>}

                {hasMore && !loading && (
                    <button onClick={() => fetchPosts(page + 1)}>Load more</button>
                )}
            </div>
        </>
    )
}