import { useState } from "react";
import { useUserEdit } from "../../hooks/useUserEdit"
import { useImageInput } from "../../hooks/useImageInput";

export const ChangeProfilePictureForm = () => {

    const { changeProfilePicture, status, error } = useUserEdit();
    const { imageFile, imagePreview, selectImage, resetImage } = useImageInput();

    const [success, setSuccess] = useState(false);

    const submit = async () => {
        if (!imageFile) return;

        const ok = await changeProfilePicture(imageFile);
        setSuccess(ok);

        if (ok) {
            resetImage();
        }
    };

    return (
        <>
        <div>
            <h3>Change Profile Picture</h3>

            {imagePreview && (
                <img src={imagePreview} alt="" />
            )}
            
            <input
                type="file"
                accept="image/*"
                onChange={e => e.target.files && selectImage(e.target.files[0])}
            />

            <div>
                <button
                    onClick={submit}
                    disabled={status === "loading" || !imageFile}
                >
                    Update
                </button>
                <button type="button" onClick={resetImage}>
                    Cancel
                </button>
            </div>

            {success && <p>Profile picture updated</p>}
            {error && <p>{error}</p>}
        </div>
        </>
    )
}