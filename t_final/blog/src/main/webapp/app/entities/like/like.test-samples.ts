import { ILike, NewLike } from './like.model';

export const sampleWithRequiredData: ILike = {
  id: 15310,
  isliked: false,
};

export const sampleWithPartialData: ILike = {
  id: 12414,
  isliked: true,
};

export const sampleWithFullData: ILike = {
  id: 11531,
  isliked: false,
};

export const sampleWithNewData: NewLike = {
  isliked: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
