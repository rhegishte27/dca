import BaseEnum from './BaseEnum';
import Feature from './Feature';

export default class Role extends BaseEnum {
    public static SYSTEM_ADMINSTRATOR = new Role('1', 'user:rolesEnum:systemAdministrator');
    public static PROJECT_MANAGER = new Role('2', 'user:rolesEnum:projectManager');
    public static DM_DEVELOPER = new Role('3', 'user:rolesEnum:dmDeveloper');
    public static BUSINESS_ANALYST = new Role('4', 'user:rolesEnum:businessAnalyst');
    public static CONVERSION_ANALYST = new Role('5', 'user:rolesEnum:conversionAnalyst');

    public level: string = '';
    public defaultFeatures: Feature[] = [];
    public nonEditableFeatures: Feature[] = [];

    public constructor(id?: string, name?: string) {
        super(id, name);
    }
}
