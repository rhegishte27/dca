import BaseEntity from './BaseEntity';
import DataObjectStatus from './DataObjectStatus';

export default class DataObjectFile extends BaseEntity {
    public id: string = '';
    public originalFileName: string = '';
    public dataObjectContent: string = '';
    public resultContent: string = '';
    public status: DataObjectStatus | undefined;
}