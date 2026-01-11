import { useState } from "react";
import { useUserEdit } from "../../hooks/useUserEdit"
import { useImageInput } from "../../hooks/useImageInput";
import styles from "./UserModifyForm.module.css"

export const ChangeProfilePictureForm = () => {

    const { changeProfilePicture, status, error } = useUserEdit();
    const { imageFile, imagePreview, removeImage, selectImage, resetImage } = useImageInput();

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
            <div className={styles.profilePictureForm}>
                <h3>Change Profile Picture</h3>

                {imagePreview && (
                    <div className={styles.imagePreviewWrapper}>
                        <img src={imagePreview} alt="Preview" className={styles.imagePreview} />
                        <button type="button" onClick={removeImage} className={styles.removeImageButton}>
                            <img src="/assets/cross.svg" alt="" />
                        </button>
                    </div>
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