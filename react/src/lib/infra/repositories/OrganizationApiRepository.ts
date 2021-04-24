import Organization from '../../domain/entities/Organization';
import OrganizationRepository from '../../domain/repositories/OrganizationRepository';
import {ApiGateway} from '../config/apiGateway';

export default class OrganizationApiRepository implements OrganizationRepository {
    readonly URL: string = '/organizations';

    constructor(private api: ApiGateway) {
    }

    async add(organization: Organization): Promise<Organization> {
        return this.api.post(this.URL, organization, {inType: Organization, outType: Organization});
    }

    async update(organization: Organization): Promise<void> {
        return this.api.put(this.URL + '/' + organization.id, organization, {inType: Organization});
    }

    async delete(id: string): Promise<void> {
        return this.api.delete(this.URL + '/' + id);
    }

    async findAll(): Promise<Organization[]> {
        return this.api.getArray(this.URL, {outType: Organization});
    }

    async findById(id: string): Promise<Organization> {
        return this.api.get(this.URL + '/' + id, {outType: Organization});
    }
}
