import BaseEntity from './BaseEntity';
import Location from './Location';
import TypeProjectElement from './TypeProjectElement';

export default class ProjectSyncSetting extends BaseEntity {
    public projectId: string = '';
    public typeProjectElement: TypeProjectElement = new TypeProjectElement();
    public isSyncEnabled: boolean = false;
    public location: Location | undefined;

    public constructor(typeProjectElement: TypeProjectElement) {
        super();
        this.typeProjectElement = typeProjectElement;
    }
}
