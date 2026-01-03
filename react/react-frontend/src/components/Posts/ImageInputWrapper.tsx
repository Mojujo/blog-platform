import {useState} from "react";
import styles from "./ImageInputWrapper.module.css"

interface Props {
    onFileSelect: (file: File) => void;
    children: React.ReactNode;
}

export const ImageInputWrapper: React.FC<Props> = ({ onFileSelect, children }) => {
    const [isDragging, setIsDragging] = useState(false);

    const handleFiles = (files: FileList | null) => {
        const file = files?.[0];
        if (file && file.type.startsWith('image/')) {
            onFileSelect(file);
        }
    };

    return (
        <div className={styles.inputWrapper}
            onPaste={(e) => handleFiles(e.clipboardData.files)}
            onDrop={(e) => { e.preventDefault(); setIsDragging(false); handleFiles(e.dataTransfer.files); }}
            onDragOver={(e) => { e.preventDefault(); setIsDragging(true); }}
            onDragLeave={() => setIsDragging(false)}
        >
            {children}
            {isDragging && <p>Drop image here</p>}
        </div>
    )
}
