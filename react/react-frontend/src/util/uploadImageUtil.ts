import { supabase } from "./supabaseClient";

export async function uploadImageUtil(file: File, userId: string) {

    const fileExt = file.name.split(".").pop();
    const fileName = `${crypto.randomUUID()}.${fileExt}`;
    const filePath = `${userId}/${fileName}`;

    const { error } = await supabase.storage
        .from("post-images")
        .upload(filePath, file);

    if (error) throw error;

    const { data } = supabase.storage
        .from("post-images")
        .getPublicUrl(filePath);

    return data.publicUrl
} 