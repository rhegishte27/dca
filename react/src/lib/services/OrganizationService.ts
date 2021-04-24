import Organization from '../domain/entities/Organization';
import OrganizationRepository from '../domain/repositories/OrganizationRepository';

export default class OrganizationService {
    constructor(private organizationRepository: OrganizationRepository) {
    }

    async add(organization: Organization): Promise<Organization> {
        return this.organizationRepository.add(organization);
    }

    async update(organization: Organization): Promise<void> {
        return this.organizationRepository.update(organization);
    }

    async delete(id: string): Promise<void> {
        return this.organizationRepository.delete(id);
    }

    async findAll(): Promise<Organization[]> {
        return this.organizationRepository.findAll();
    }

    async findById(id: string): Promise<Organization> {
        return this.organizationRepository.findById(id);
    }
}