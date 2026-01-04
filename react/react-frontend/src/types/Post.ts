export interface Post {
    id: string;
    userId: string;
    title: string;
    content: string;
    imageUrl: string | null;
    createdDate: string;
    authorUsername: string;
    isOwner: boolean;
}