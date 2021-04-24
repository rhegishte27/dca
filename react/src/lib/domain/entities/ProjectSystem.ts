import BaseEntity from './BaseEntity';
import Location from './Location';
import System from './System';
import SystemType from './SystemType';

export default class ProjectSystem extends BaseEntity {
    public projectId: string = '';
    public systemType: SystemType = new SystemType();
    public system: System = new System();
    public isSynchronizationEnabled: boolean = false;
    public location: Location | undefined;
}
