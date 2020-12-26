package com.filesystem.models;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name="Files", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "folderId"})
})
@NamedQueries({
        @NamedQuery(name = "findFile", query = "SELECT f FROM File f WHERE f.name = :name and ((:folder is null and f.folder is null) or f.folder = :folder)"),
})
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 100000)
    private byte[] data = new byte[0];

    @Transient
    private Long folderId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="folderId")
    private Folder folder;

    public File() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }
}
