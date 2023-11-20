import { IUser } from 'app/entities/user/user.model';
import { IPost } from 'app/entities/post/post.model';

export interface IComment {
  id: number;
  content?: string | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
  post?: Pick<IPost, 'id' | 'title'> | null;
}

export type NewComment = Omit<IComment, 'id'> & { id: null };
