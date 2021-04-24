import Project from '../entities/Project';

export default interface ProjectRepository {

    add(project: Project): Promise<Project>;

    update(project: Project): Promise<void>;

    delete(id: string): Promise<void>;

    findAll(): Promise<Project[]>;

    findById(id: string): Promise<Project>;
}