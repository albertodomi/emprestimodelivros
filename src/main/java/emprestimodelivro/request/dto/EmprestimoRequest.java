package emprestimodelivro.request.dto;

import java.util.List;

public class EmprestimoRequest {
    private Long usuarioId;
    private List<Long> livroIds;
    
    // Construtor padrão
    public EmprestimoRequest() {
    }
    
    // Construtor com parâmetros
    public EmprestimoRequest(Long usuarioId, List<Long> livroIds) {
        this.usuarioId = usuarioId;
        this.livroIds = livroIds;
    }
    
    // Getters e Setters
    public Long getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    public List<Long> getLivroIds() {
        return livroIds;
    }
    
    public void setLivroIds(List<Long> livroIds) {
        this.livroIds = livroIds;
    }
    
    // toString para facilitar o debug
    @Override
    public String toString() {
        return "EmprestimoRequest{" +
                "usuarioId=" + usuarioId +
                ", livroIds=" + livroIds +
                '}';
    }
    
    // equals e hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        EmprestimoRequest that = (EmprestimoRequest) o;
        
        if (usuarioId != null ? !usuarioId.equals(that.usuarioId) : that.usuarioId != null) return false;
        return livroIds != null ? livroIds.equals(that.livroIds) : that.livroIds == null;
    }
    
    @Override
    public int hashCode() {
        int result = usuarioId != null ? usuarioId.hashCode() : 0;
        result = 31 * result + (livroIds != null ? livroIds.hashCode() : 0);
        return result;
    }
}