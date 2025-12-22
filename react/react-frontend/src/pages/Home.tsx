import { useFeed } from "../hooks/useFeed"
import styles from "./Home.module.css"

export default function Home() {
    const { posts, loading, fetchPosts, page, hasMore} = useFeed();

    return (
        <>
        <div className={styles.feedContainer}>
            <h1>Recent Posts</h1>

            {posts.length === 0 && !loading && <p>No posts yet</p> }

            <ul>
                {posts.map(post => (
                    <li key={post.id}>
                        <h3>{post.title}</h3>
                        <p>{post.content}</p>
                        <small>
                            By {post.authorUsername} - {new Date(post.createdDate).toLocaleString()} 
                        </small>
                    </li>
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
// TODO ADD AUTHOR TO POSTS