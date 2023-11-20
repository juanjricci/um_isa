import { IBlog, NewBlog } from './blog.model';

export const sampleWithRequiredData: IBlog = {
  id: 12049,
  name: 'stage darling yahoo',
  handle: 'yum as string',
};

export const sampleWithPartialData: IBlog = {
  id: 32376,
  name: 'hook',
  handle: 'vivacious carelessly safely',
};

export const sampleWithFullData: IBlog = {
  id: 7061,
  name: 'geez worse stallion',
  handle: 'gasoline reprint',
};

export const sampleWithNewData: NewBlog = {
  name: 'angrily throughout magnificent',
  handle: 'drat firm anime',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
