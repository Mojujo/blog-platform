import { Post } from "../types/Post";
import apiClient from "../api/apiClient";

export const useEditDeletePost = (
    setPosts: React.Dispatch<React.SetStateAction<Post[]>> ) => {

    const editPost = async (updatedPost: Post) => {
        const response = await apiClient.put(`/post/${updatedPost.id}`, {
            title: updatedPost.title,
            content: updatedPost.content,
            imageUrl: updatedPost.imageUrl
        });

        const newPost: Post = response.data
        setPosts(prev => prev.map(p => (p.id === newPost.id ? newPost : p)));
    };

    const deletePost = async (postId: string) => {
        await apiClient.delete(`/post/${postId}`);
        setPosts(prev => prev.filter(p => p.id !== postId));
    };

    return { editPost, deletePost }
}