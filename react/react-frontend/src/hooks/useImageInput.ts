import { useEffect, useState } from "react";

export function useImageInput(initialUrl?: string | null) {

    const [imageFile, setImageFile] = useState<File | null>(null);
    const [imagePreview, setImagePreview] = useState<string | null>(initialUrl ?? null);
    const [shouldRemoveImage, setShouldRemoveImage] = useState(false);

    const selectImage = (file: File) => {

        revokeUrl();
        setImageFile(file);
        setImagePreview(URL.createObjectURL(file));
        setShouldRemoveImage(false);

    };

    const removeImage = () => {

        revokeUrl();
        setImageFile(null);
        setImagePreview(null);
        setShouldRemoveImage(true);

    };

    const resetImage = () => {

        revokeUrl();
        setImageFile(null);
        setImagePreview(initialUrl ?? null);
        setShouldRemoveImage(false);

    }

    const revokeUrl = () => {
        if(imagePreview && imagePreview !== initialUrl) {
            URL.revokeObjectURL(imagePreview);
        }
    }

    useEffect(() => {
        return () => {
            revokeUrl();
        }
    }, [imagePreview, initialUrl])

    return {
        imageFile,
        imagePreview,
        selectImage,
        removeImage,
        shouldRemoveImage,
        resetImage,
        revokeUrl
    }
}