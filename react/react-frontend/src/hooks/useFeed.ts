import { useEffect, useState } from "react";
import apiClient from "../api/apiClient";
import { Post } from "../types/Post";

export const useFeed = (pageSize: number = 10) => {
    const [posts, setPosts] = useState<Post[]>([]);
    const [loading, setLoading] = useState(false);
    const [page, setPage] = useState(0);
    const [hasMore, setHasMore] = useState(true);

    const fetchPosts = async (nextPage: number) => {

        setLoading(true);

        try {
            const response = await apiClient.get(`/post/feed?page=${nextPage}&size=${pageSize}`);
            const data: Post[] = response.data.content
            setPosts(prev => nextPage === 0 ? data : [...prev, ...data]);
            setHasMore(!response.data.last);
            setPage(nextPage);
        } catch (err : any) {
            console.error("Failed to fetch feed", err)
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchPosts(0);
    }, []);

    return { posts, setPosts, loading, fetchPosts, page, hasMore}
}