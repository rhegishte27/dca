import BaseEntity from './BaseEntity';
import DataObject from './DataObject';

export default class System extends BaseEntity {
    public id: string = '';
    public identifier: string = '';
    public description: string = '';
    public dataObjects: DataObject[] = [];
}
