import {useState} from "react";

interface Props {
    onFileSelect: (file: File) => void;
    children: React.ReactNode;
}

export const ImageUploadWrapper: React.FC<Props> = ({ onFileSelect, children }) => {
    const [isDragging, setIsDragging] = useState(false);

    const handleFiles = (files: FileList | null) => {
        const file = files?.[0];
        if (file && file.type.startsWith('image/')) {
            onFileSelect(file);
        }
    };

    return (
        <div
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
