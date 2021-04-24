import BaseEntity from './BaseEntity';
import DataObject from './DataObject';
import DataObjectFile from './DataObjectFile';
import DataObjectLocationType from './DataObjectLocationType';
import DataObjectType from './DataObjectType';
import Location from './Location';
import System from './System';

export default class DataObjectContainer extends BaseEntity {
    public system: System = new System();
    public locationType: DataObjectLocationType | undefined;
    public type: DataObjectType | undefined;
    public location: Location | undefined;

    public dataObjectFileList: DataObjectFile[] = [];
    public dataObjectFileListChosenInCreateForm: DataObjectFile[] = [];
    public dataObjectListChosenInImportForm: DataObject[] = [];
    public dataObjectListImported: DataObject[] = [];

    public constructor(system?: System) {
        super();
        if (system) {
            this.system = system;
        }
    }
}