import { INotification, NewNotification } from './notification.model';

export const sampleWithRequiredData: INotification = {
  id: 25946,
  content: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: INotification = {
  id: 25773,
  content: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: INotification = {
  id: 20124,
  content: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewNotification = {
  content: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
