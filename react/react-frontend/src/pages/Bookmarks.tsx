import PostItem from "../components/Posts/PostItem";
import { useEditDeletePost } from "../hooks/useEditDeletePost";
import { useFetchBookmarks } from "../hooks/useFetchBookmarks";
import styles from "./Bookmarks.module.css"

export default function Bookmarks() {

    const { posts, loading, setPosts } = useFetchBookmarks();
    const { editPost, deletePost } = useEditDeletePost(setPosts);

    return (
        <ul className={styles.bookmarksContainer}>
            {posts.length === 0 && !loading && <h3>No bookmarks yet</h3>}
            {loading && <p>Loading... </p>}

            {posts.map(post => (
                <PostItem
                    key={post.id}
                    post={post}
                    onEdit={editPost}
                    onDelete={deletePost}
                    showAuthor
                />
            ))}
        </ul>
    )
}