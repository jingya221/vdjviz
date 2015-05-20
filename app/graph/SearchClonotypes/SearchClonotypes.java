package graph.SearchClonotypes;

import controllers.AccountAPI;
import graph.AnnotationTable.AnnotationTableRow;
import models.UserFile;
import utils.BinaryUtils.ClonotypeBinaryUtils.ClonotypeBinary;
import utils.BinaryUtils.ClonotypeBinaryUtils.ClonotypeBinaryContainer;
import utils.BinaryUtils.ClonotypeBinaryUtils.ClonotypeFilters.*;

import java.util.ArrayList;
import java.util.List;

public class SearchClonotypes {
    private final static int searchCount = 100;

    static class SingleSampleSearchResults {
        public List<AnnotationTableRow> rows;
        public String fileName;
        public AccountAPI.SearchClonotypesRequest parameters;

        public SingleSampleSearchResults(List<AnnotationTableRow> rows, String fileName, AccountAPI.SearchClonotypesRequest parameters) {
            this.rows = rows;
            this.fileName = fileName;
            this.parameters = parameters;
        }
    }

    public static SingleSampleSearchResults searchSingleSample(UserFile file, AccountAPI.SearchClonotypesRequest searchClonotypesRequest) {
        List<AnnotationTableRow> rows = new ArrayList<>();
        ClonotypeBinaryContainer container = new ClonotypeBinaryContainer(file.getDirectoryPath() + "/clonotype.bin");

        List<BinaryClonotypeFilter> filters = new ArrayList<>();
        if (searchClonotypesRequest.vFilter.length > 0)
            filters.add(new BinaryClonotypeVFilter(searchClonotypesRequest.vFilter));
        if (searchClonotypesRequest.jFilter.length > 0)
            filters.add(new BinaryClonotypeJFilter(searchClonotypesRequest.jFilter));
        filters.add(new BinaryClonotypeSequenceFilter(searchClonotypesRequest.sequence, searchClonotypesRequest.aminoAcid));

        FilteredBinaryClonotypes filteredBinaryClonotypes = new FilteredBinaryClonotypes(container, filters);

        int index = 1;
        for (ClonotypeBinary clonotype : filteredBinaryClonotypes.getFiltered(searchCount)) {
            rows.add(new AnnotationTableRow(clonotype, index));
            index++;
        }
        return new SingleSampleSearchResults(rows, file.getFileName(), searchClonotypesRequest);
    }

    public static List<SingleSampleSearchResults> searchMultipleSample(List<UserFile> files, AccountAPI.SearchClonotypesRequest searchClonotypesRequest) {
        List<SingleSampleSearchResults> results = new ArrayList<>();
        for (UserFile file : files) {
            results.add(searchSingleSample(file, searchClonotypesRequest));
        }
        return results;
    }

}