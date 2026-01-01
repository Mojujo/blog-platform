import { Post } from "../../types/Post";

type Props = {
    post: Post;
    onEdit: (post: Post) => void;
    onDelete: (id: string) => void;
}

export default function PostItem({ post, onEdit, onDelete}: Props) {
    return (
        <li>
            <h3>{post.title}</h3>
            <p>{post.content}</p>

            <small>
                By {post.authorUsername} ·{" "}
                {new Date(post.createdDate).toLocaleString()}
            </small>

            {post.isOwner && (
                <div>
                    <button onClick={() => onEdit?.(post)}>Edit</button>
                    <button onClick={() => onDelete?.(post.id)}></button>
                </div>
            )}
        </li>
    )
}