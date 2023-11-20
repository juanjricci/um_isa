import dayjs from 'dayjs/esm';

import { IPost, NewPost } from './post.model';

export const sampleWithRequiredData: IPost = {
  id: 5339,
  title: 'silently fiercely boohoo',
  content: '../fake-data/blob/hipster.txt',
  date: dayjs('2023-11-10T06:21'),
  likes: 20236,
};

export const sampleWithPartialData: IPost = {
  id: 15441,
  title: 'urgently ugh',
  content: '../fake-data/blob/hipster.txt',
  date: dayjs('2023-11-09T19:22'),
  likes: 20894,
};

export const sampleWithFullData: IPost = {
  id: 21707,
  title: 'to',
  content: '../fake-data/blob/hipster.txt',
  date: dayjs('2023-11-10T07:06'),
  likes: 2569,
};

export const sampleWithNewData: NewPost = {
  title: 'nor',
  content: '../fake-data/blob/hipster.txt',
  date: dayjs('2023-11-10T13:59'),
  likes: 6103,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
