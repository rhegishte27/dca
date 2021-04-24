import BaseEntity from './BaseEntity';
import DataObjectFile from './DataObjectFile';
import DataObjectStatus from './DataObjectStatus';
import DataObjectType from './DataObjectType';
import System from './System';

export default class DataObject extends BaseEntity {
    public id: string = '';
    public identifier: string = '';
    public description: string = '';
    public systemId: string = '';
    public systemIdentifier: string = '';
    public type: DataObjectType | undefined;
    public status: DataObjectStatus | undefined;
    public dataObjectFiles: DataObjectFile[] = [];

    public constructor(system?: System) {
        super();
        if (system) {
            this.systemId = system.id;
        }
    }
}
