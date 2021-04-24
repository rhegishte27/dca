import Project from '../../domain/entities/Project';
import ProjectRepository from '../../domain/repositories/ProjectRepository';
import {ApiGateway} from '../config/apiGateway';

export default class ProjectApiRepository implements ProjectRepository {
    readonly URL: string = '/projects';

    constructor(private api: ApiGateway) {
    }

    async add(project: Project): Promise<Project> {
        return this.api.post(this.URL, project, {inType: Project, outType: Project});
    }

    async update(project: Project): Promise<void> {
        return this.api.put(this.URL + '/' + project.id, project, {inType: Project});
    }

    async delete(id: string): Promise<void> {
        return this.api.delete(this.URL + '/' + id);
    }

    async findAll(): Promise<Project[]> {
        return this.api.getArray(this.URL, {outType: Project});
    }

    async findById(id: string): Promise<Project> {
        return this.api.get(this.URL + '/' + id, {outType: Project});
    }
}
