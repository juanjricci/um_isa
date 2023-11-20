import { IPost } from 'app/entities/post/post.model';

export interface ILike {
  id: number;
  isliked?: boolean | null;
  post?: Pick<IPost, 'id' | 'title'> | null;
}

export type NewLike = Omit<ILike, 'id'> & { id: null };
