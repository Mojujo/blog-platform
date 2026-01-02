import { useState } from "react";
import { Post } from "../../types/Post";

type Props = {
    post: Post;
    onEdit: (post: Post) => void;
    onDelete: (id: string) => void;
    showAuthor?: boolean;
}

export default function PostItem({ post, onEdit, onDelete, showAuthor }: Props) {

    const [isEditing, setIsEditing] = useState(false);
    const [title, setTitle] = useState(post.title);
    const [content, setContent] = useState(post.content);

    const handleSave = () => {
        onEdit({ ...post, title, content });
        setIsEditing(false);
    }

    const handleCancel = () => {
        setTitle(post.title);
        setContent(post.content);
        setIsEditing(false);
    }

    return (
        <li>
            {isEditing ? (
                <div>
                    <input
                        type="text"
                        name="edit-title"
                        value={title}
                        onChange={e => setTitle(e.target.value)}
                        placeholder="Title"
                    />
                    <textarea
                        value={content}
                        name="edit-content"
                        onChange={e => setContent(e.target.value)}
                        placeholder="Content"
                    />
                    <button onClick={handleSave}>Save</button>
                    <button onClick={handleCancel}>Cancel</button>
                </div>
            ) : (
                <>
                    <h3>{post.title}</h3>
                    <p>{post.content}</p>

                    {post.imageUrl && (
                        <img
                            src={post.imageUrl}
                            alt={post.title}
                        />
                    )}

                    <small>
                        {showAuthor ? `By ${post.authorUsername} · ` : ""}
                        {new Date(post.createdDate).toLocaleString()}
                    </small>

                    {post.isOwner && (
                        <div>
                            <button onClick={() => setIsEditing(true)}>Edit</button>
                            <button onClick={() => onDelete?.(post.id)}>Delete</button>
                        </div>
                    )}
                </>
            )}
        </li>
    )
}