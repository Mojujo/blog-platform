import { useEffect, useState } from "react";
import apiClient from "../api/apiClient";
import { useAuth } from "../context/AuthContext";
import { Post } from "../types/Post";

export const useProfileFeed = (pageSize: number = 10) => {
    const { user, authLoaded } = useAuth();
    const [posts, setPosts] = useState<Post[]>([]);
    const [loading, setLoading] = useState(false);
    const [page, setPage] = useState(0);
    const [hasMore, setHasMore] = useState(true);

    const fetchPosts = async (nextPage: number) => {

        setLoading(true);

        try {
            const response = await apiClient.get(`/post?page=${nextPage}&size=${pageSize}`);
            const data: Post[] = response.data.content
            setPosts(prev => nextPage === 0 ? data : [...prev, ...data]);
            setHasMore(!response.data.last);
            setPage(nextPage);
        } catch (err: any) {
            console.error("Failed to fetch feed", err)

            if (err.response?.status === 403) {
                setPosts([]);
                setHasMore(false);
            }

        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {

        if (!authLoaded) return;
        if (!user) {
            setPosts([]);
            return;
        }
        fetchPosts(0);
    }, [user, authLoaded]);

    return { posts, loading, fetchPosts, page, hasMore }
}