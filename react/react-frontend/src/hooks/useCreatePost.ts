import { useState } from "react";
import apiClient from "../api/apiClient";

export interface PostPayLoad {
    title: string;
    content: string;
    imageUrl?: string;
}

export const useCreatePost = () => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const createPost = async (payload: PostPayLoad) => {
        setLoading(true);
        setError(null);

        try {
            await apiClient.get("/user/csrf");

            const response = await apiClient.post("/post", payload);
            setLoading(false);
            return response.data
        } catch (err: any) {
            setLoading(false)
            setError(err.response?.data?.message || "Failed to create post");
            return null;
        }
    };

    return { createPost, loading, error };
}