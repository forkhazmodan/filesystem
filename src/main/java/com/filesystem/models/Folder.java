package com.filesystem.models;


import com.sun.istack.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Table(name="Folders", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name", "parentId"})
})
@NamedQueries({
        @NamedQuery(name = "findFolder", query = "SELECT f FROM Folder f WHERE f.name = :name and ((:parent is null and f.parent is null) or f.parent = :parent)"),
})
@Entity
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @Transient
    private Long parentId;

    @ManyToOne(fetch = FetchType.EAGER, optional=true)
    @JoinColumn(name="parentId")
    private Folder parent;

    @OneToMany(mappedBy="parent", fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE) //TODO: Find out is this a bug? https://stackoverflow.com/questions/41465852/ondelete-hibernate-annotation-does-not-generate-on-delete-cascade-for-mysql
    private Set<Folder> children = new HashSet<>();

    @OneToMany(mappedBy="folder", fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE) //TODO: Find out is this a bug? https://stackoverflow.com/questions/41465852/ondelete-hibernate-annotation-does-not-generate-on-delete-cascade-for-mysql
    private Set<File> files = new HashSet<>();

    public Folder() {
    }

    public Folder(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public void setParent(Folder parent) {
        this.parent = parent;
    }

    public Folder getParent() {
        return parent;
    }

    public void setChildren(Set<Folder> children) {
        this.children = children;
    }

    public Set<Folder> getChildren() {
        // TODO: implement sorting by name
        return children;
    }

    public void addChildren(Folder folder) {
        if ( ! getChildren().contains(folder)) {
            children.add(folder);
            folder.setParent(this);
        }
    }

    public void setFiles(Set<File> files) {
        this.files = files;
    }

    public Set<File> getFiles() {
        return files;
    }

    public void addFile(File file) {
        if ( ! getFiles().contains(file)) {
            files.add(file);
        }
    }

    @Override
    public String toString() {
        return "Folder{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", parent=" + parent +
                '}';
    }
}