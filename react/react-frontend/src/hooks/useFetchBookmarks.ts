import { useEffect, useState } from "react";
import { useBookmarks } from "../context/BookmarkContext"
import { Post } from "../types/Post";
import apiClient from "../api/apiClient";

export const useFetchBookmarks = () => {

    const { bookmarks } = useBookmarks();
    const [posts, setPosts] = useState<Post[]>([]);
    const [loading, setLoading] = useState(false);

    const fetchPosts = async () => {
        if (!bookmarks.size) {
            setPosts([]);
            return;
        }

        setLoading(true);
        try {
            const ids = Array.from(bookmarks);
            const responses = await Promise.all(ids.map(id => apiClient.get(`/post/${id}`)));
            setPosts(responses.map(res => res.data as Post));
        } catch (err: any) {
            console.error("Failed to fetch bookmarked posts", err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchPosts();
    }, [bookmarks]);

    return { posts, setPosts, loading, fetchPosts };
}