import { createContext, ReactNode, useContext, useEffect, useState } from "react";

type BookmarkContextType = {
    bookmarks: Set<string>;
    toggleBookmark: (postId: string) => void;
    isBookmarked: (postId: string) => boolean;
};

const BookmarkContext = createContext<BookmarkContextType | undefined>(undefined);

export const BookmarkProvider = ({ children }: { children: ReactNode }) => {
    const [bookmarks, setBookmarks] = useState<Set<string>>(new Set());

    useEffect(() => {
        const stored = localStorage.getItem("bookmarks");
        if (stored) {
            setBookmarks(new Set(JSON.parse(stored)));
        }
    }, []);

    useEffect(() => {
        localStorage.setItem("bookmarks", JSON.stringify(Array.from(bookmarks)));
    }, [bookmarks]);

    const toggleBookmark = (postId: string) => {
        setBookmarks(prev => {
            const updated = new Set(prev);
            if (updated.has(postId)) updated.delete(postId);
            else updated.add(postId);
            return updated;
        });
    };

    const isBookmarked = (postId: string) => bookmarks.has(postId);

    return (
        <BookmarkContext.Provider value = {{
            bookmarks,
            toggleBookmark,
            isBookmarked
        }}>
            {children}
        </BookmarkContext.Provider>
    )
};

export const useBookmarks = () => {
    const ctx = useContext(BookmarkContext);
    if (!ctx) throw new Error("Must be used within Bookmarkprovider");
    return ctx;
}