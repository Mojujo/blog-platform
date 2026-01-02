export interface Post {
    id: string;
    userId: string;
    title: string;
    content: string;
    imageUrl: string;
    createdDate: string;
    authorUsername: string;
    isOwner: boolean;
}