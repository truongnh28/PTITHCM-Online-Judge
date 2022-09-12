package ptithcm.onlinejudge.model.response;

import java.time.Instant;
import java.util.List;

public class CloudinaryResponse {
    String public_id;
    Long version;
    String signature;
    String resource_type;
    Instant createAt;
    List<Object> tags;
    Long bytes;
    String type;
    String eTag;
    String url;
    String secure_url;
    String original_filename;

    public CloudinaryResponse(String public_id, Long version, String signature, String resource_type, Instant createAt,
                              List<Object> tags, Long bytes, String type, String eTag, String url, String secure_url, String original_filename) {
        this.public_id = public_id;
        this.version = version;
        this.signature = signature;
        this.resource_type = resource_type;
        this.createAt = createAt;
        this.tags = tags;
        this.bytes = bytes;
        this.type = type;
        this.eTag = eTag;
        this.url = url;
        this.secure_url = secure_url;
        this.original_filename = original_filename;
    }

    public String getPublic_id() {
        return public_id;
    }

    public void setPublic_id(String public_id) {
        this.public_id = public_id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getResource_type() {
        return resource_type;
    }

    public void setResource_type(String resource_type) {
        this.resource_type = resource_type;
    }

    public Instant getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public List<Object> getTags() {
        return tags;
    }

    public void setTags(List<Object> tags) {
        this.tags = tags;
    }

    public Long getBytes() {
        return bytes;
    }

    public void setBytes(Long bytes) {
        this.bytes = bytes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String geteTag() {
        return eTag;
    }

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSecure_url() {
        return secure_url;
    }

    public void setSecure_url(String secure_url) {
        this.secure_url = secure_url;
    }

    public String getOriginal_filename() {
        return original_filename;
    }

    public void setOriginal_filename(String original_filename) {
        this.original_filename = original_filename;
    }
}
