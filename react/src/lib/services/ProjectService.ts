import Project from '../domain/entities/Project';
import ProjectRepository from '../domain/repositories/ProjectRepository';

export default class ProjectService {
    constructor(private repository: ProjectRepository) {
    }

    async add(project: Project): Promise<Project> {
        return this.repository.add(project);
    }

    async update(project: Project): Promise<void> {
        return this.repository.update(project);
    }

    async delete(id: string): Promise<void> {
        return this.repository.delete(id);
    }

    async findAll(): Promise<Project[]> {
        return this.repository.findAll();
    }

    async findById(id: string): Promise<Project> {
        return this.repository.findById(id);
    }
}