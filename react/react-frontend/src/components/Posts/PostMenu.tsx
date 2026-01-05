import { useEffect, useRef, useState } from "react";
import styles from "./PostMenu.module.css";

type Props = {
    onEdit: () => void;
    onDelete: () => void;
};

export function PostMenu({ onEdit, onDelete }: Props) {
    const [isOpen, setIsOpen] = useState(false);
    const menuRef = useRef<HTMLDivElement>(null);

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
                    <button onClick={() => { onEdit(); setIsOpen(false); }}>
                        Edit
                    </button>
                    <button
                        className={styles.deleteButton}
                        onClick={() => { onDelete(); setIsOpen(false); }}
                    >
                        Delete
                    </button>
                </div>
            )}
        </div>
    );
}