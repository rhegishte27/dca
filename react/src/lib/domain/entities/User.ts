import BaseEntity from './BaseEntity';
import Feature from './Feature';
import Language from './Language';
import Organization from './Organization';
import Role from './Role';

export default class User extends BaseEntity {
    public id: string = '';
    public organization: Organization = new Organization();
    public role: Role = new Role();
    public identifier: string = '';
    public name: string = '';
    public password: string = '';
    public emailAddress: string = '';
    public language: Language | undefined;
    public features: Feature[] = [];
}
