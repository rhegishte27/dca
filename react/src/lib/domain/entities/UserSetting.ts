import DataObject from './DataObject';
import Organization from './Organization';
import Project from './Project';
import System from './System';
import User from './User';
import Location from './Location';

export default class UserSetting {
    public id: string = '';
    public user: User = new User();
    public defaultSystem: System | undefined;
    public defaultProject: Project | undefined;
    public defaultOrganization: Organization | undefined;
    public defaultUser: User | undefined;
    public defaultLocation: Location | undefined;
    public defaultDataObject: DataObject | undefined;
}
