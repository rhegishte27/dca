import Compiler from '../entities/Compiler';

export default interface CompilerRepository {
    findAll(): Promise<Compiler[]>;
}