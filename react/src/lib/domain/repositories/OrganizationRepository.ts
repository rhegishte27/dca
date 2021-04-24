import Organization from '../entities/Organization';

export default interface OrganizationRepository {
    add(organization: Organization): Promise<Organization>;

    update(organization: Organization): Promise<void>;

    delete(id: string): Promise<void>;

    findAll(): Promise<Organization[]>;

    findById(id: string): Promise<Organization>;
}