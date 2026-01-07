import { useEffect, useRef, useState } from "react";
import styles from "./PostMenu.module.css";
import { Post } from "../../types/Post";
import { useBookmarks } from "../../context/BookmarkContext";

type Props = {
    post: Post;
    onEdit: () => void;
    onDelete: () => void;
    isOwner: boolean;
};

export function PostMenu({ post, onEdit, onDelete, isOwner }: Props) {

    const [isOpen, setIsOpen] = useState(false);
    const menuRef = useRef<HTMLDivElement>(null);

    const { toggleBookmark, isBookmarked } = useBookmarks();

    useEffect(() => {
        function handleOutsideClick(event: MouseEvent) {
            if (menuRef.current && !menuRef.current.contains(event.target as Node)) {
                setIsOpen(false);
            }
        }

        if (isOpen) {
            document.addEventListener("mousedown", handleOutsideClick);
        }
        return () => document.removeEventListener("mousedown", handleOutsideClick);
    }, [isOpen]);

    return (
        <div className={styles.menuContainer} ref={menuRef}>
            <button
                className={styles.menuButton}
                onClick={() => setIsOpen(!isOpen)}
                aria-label="Options"
            >
                <img src="/assets/postmenu.svg" alt="menu" />
            </button>

            {isOpen && (
                <div className={styles.dropdown}>
                    <button className={styles.bookmarkButton}
                        onClick={() => {toggleBookmark(post.id)}}>
                        {isBookmarked(post.id) ? "Bookmark ★" : "Bookmark ☆"}
                    </button>
                    {post.isOwner && (
                        <>
                            <button
                                onClick={() => { onEdit(); setIsOpen(false); }}>
                                Edit
                            </button>
                            <button className={styles.deleteButton}
                                onClick={() => { onDelete(); setIsOpen(false); }}>
                                Delete
                            </button>
                        </>
                    )}
                </div>
            )}
        </div>
    );
}