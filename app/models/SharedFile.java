package models;

import com.antigenomics.vdjtools.misc.Software;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import play.Logger;
import play.db.ebean.Model;
import org.apache.commons.io.FileDeleteStrategy;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.File;
import java.io.IOException;

@Entity
public class SharedFile extends Model {
    @Id
    private Long id;
    @ManyToOne
    private SharedGroup group;
    private String fileName;
    private String uniqueName;
    private Integer clonotypesCount;
    private Long sampleCount;
    private Software softwareType;
    private String softwareTypeName;
    private String filePath;
    private String fileDirPath;
    private DateTime createdAt;


    public SharedFile(UserFile file, SharedGroup group, String uniqueName, String filePath, String fileDirPath) {
        this.group = group;
        this.fileName = file.getFileName();
        this.clonotypesCount = file.getClonotypesCount();
        this.sampleCount = file.getSampleCount();
        this.softwareType = file.getSoftwareType();
        this.softwareTypeName = file.getSoftwareTypeName();
        this.uniqueName = uniqueName;
        this.filePath = filePath;
        this.fileDirPath = fileDirPath;
        this.createdAt = new DateTime();
    }

    public UserFile.FileInformation getFileInformation() {
        return new UserFile.FileInformation(this);
    }

    public void deleteFile() {
        File fileDir = new File(fileDirPath);
        File[] files = fileDir.listFiles();
        if (files == null) {
            fileDir.delete();
            this.delete();
            return;
        }
        for (File cache : files) {
            try {
                FileDeleteStrategy.FORCE.delete(cache);
                //Files.deleteIfExists(cache.toPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        fileDir.delete();
        this.delete();
    }

    public SharedGroup getGroup() {
        return group;
    }

    public String getFileName() {
        return fileName;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public Integer getClonotypesCount() {
        return clonotypesCount;
    }

    public Long getSampleCount() {
        return sampleCount;
    }

    public Software getSoftwareType() {
        return softwareType;
    }

    public String getSoftwareTypeName() {
        return softwareTypeName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileDirPath() {
        return fileDirPath;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }
}
