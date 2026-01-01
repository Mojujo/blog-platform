import { Post } from "../../types/Post";

type Props = {
    post: Post;
    onEdit: (post: Post) => void;
    onDelete: (id: string) => void;
    showAuthor?: boolean;
}

export default function PostItem({ post, onEdit, onDelete, showAuthor }: Props) {
    return (
        <li>
            <h3>{post.title}</h3>
            <p>{post.content}</p>

            <small>
                {showAuthor ? `By ${post.authorUsername} · ` : ""}
                {new Date(post.createdDate).toLocaleString()}
            </small>

            {post.isOwner && (
                <div>
                    <button onClick={() => onEdit?.(post)}>Edit</button>
                    <button onClick={() => onDelete?.(post.id)}>Delete</button>
                </div>
            )}
        </li>
    )
}