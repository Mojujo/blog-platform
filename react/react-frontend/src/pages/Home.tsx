import PostItem from "../components/Posts/PostItem";
import { useFeed } from "../hooks/useFeed";
import { useEditDeletePost } from "../hooks/useEditDeletePost";
import styles from "./Home.module.css";
import Explore from "../components/Navigation/Explore";

export default function Home() {
    const { posts, setPosts, loading, fetchPosts, page, hasMore } = useFeed();
    const { editPost, deletePost } = useEditDeletePost(setPosts);

    return (
        <>
            <ul className={styles.feedList}>
                {posts.length === 0 && !loading && <p>No posts yet</p>}

                {loading && <p>Loading... </p>}

                {posts.map(post => (
                    <PostItem
                        key={post.id}
                        post={post}
                        onEdit={editPost}
                        onDelete={deletePost}
                        showAuthor={true}
                    />
                ))}

                {hasMore && !loading && (
                    <button onClick={() => fetchPosts(page + 1)}>Load more</button>
                )}
            </ul>
        </>
    )
}