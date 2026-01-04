import { useState } from "react";
import { Post } from "../../types/Post";
import { useImageInput } from "../../hooks/useImageInput";
import { uploadImageUtil } from "../../util/uploadImageUtil";
import { ImageInputWrapper } from "./ImageInputWrapper";

type Props = {
    post: Post;
    onEdit: (post: Post) => void;
    onDelete: (id: string) => void;
    showAuthor?: boolean;
}

export default function PostItem({ post, onEdit, onDelete, showAuthor }: Props) {

    const { imageFile, imagePreview, shouldRemoveImage, selectImage, removeImage, resetImage } = useImageInput(post.imageUrl);

    const [isEditing, setIsEditing] = useState(false);
    const [title, setTitle] = useState(post.title);
    const [content, setContent] = useState(post.content);

    const handleSave = async () => {

        let imageUrl = post.imageUrl;

        if (imageFile) {
            imageUrl = await uploadImageUtil(imageFile);
        }

        if (shouldRemoveImage) {
            imageUrl = null;
        }

        onEdit({ ...post, title, content, imageUrl });
        setIsEditing(false);
    }

    const handleCancel = () => {
        setTitle(post.title);
        setContent(post.content);
        resetImage();
        setIsEditing(false);
    }

    return (
        <li>
            {isEditing ? (
                <ImageInputWrapper onFileSelect={selectImage}>
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

                        <input
                            type="file"
                            accept="image/*"
                            onChange={(e) => {
                                const file = e.target.files?.[0];
                                if (file) {
                                    selectImage(file);
                                }
                            }}
                        />

                        {imagePreview && (
                            <>
                                <img src={imagePreview} alt="Preview" />
                                <button type="button" onClick={removeImage}>Remove image</button>
                            </>
                        )}

                        <button onClick={handleSave}>Save</button>
                        <button onClick={handleCancel}>Cancel</button>
                    </div>
                </ImageInputWrapper>
            ) : (
                <>
                    <div>
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
                    </div>
                </>
            )}
        </li>
    )
}