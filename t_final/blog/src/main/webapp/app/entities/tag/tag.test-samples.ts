import { ITag, NewTag } from './tag.model';

export const sampleWithRequiredData: ITag = {
  id: 29793,
  name: 'blah',
};

export const sampleWithPartialData: ITag = {
  id: 19617,
  name: 'triumphantly rekindle politely',
};

export const sampleWithFullData: ITag = {
  id: 24432,
  name: 'on below once',
};

export const sampleWithNewData: NewTag = {
  name: 'yum untrue worth',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
