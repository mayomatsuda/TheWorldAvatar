package uk.ac.cam.cares.jps.composition.Vocabulary;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

public enum MSM {

	Service() {
		@Override
		public String IRI() {
			return msm + this.name();
		}

		@Override
		public Resource Node() {
			Model model = ModelFactory.createDefaultModel();
			return model.createResource(msm + this.name());
		}

		@Override
		public com.hp.hpl.jena.rdf.model.Property Property() {
			// TODO Auto-generated method stub
			return null;
		}

	},
	Operation() {
		@Override
		public String IRI() {
			return msm + this.name();
		}

		@Override
		public Resource Node() {
			Model model = ModelFactory.createDefaultModel();
			return model.createResource(msm + this.name());
		}

		@Override
		public com.hp.hpl.jena.rdf.model.Property Property() {
			// TODO Auto-generated method stub
			return null;
		}

	},
	MessagePart() {
		@Override
		public String IRI() {
			return msm + this.name();
		}

		@Override
		public Resource Node() {
			Model model = ModelFactory.createDefaultModel();
			return model.createResource(msm + this.name());
		}

		@Override
		public com.hp.hpl.jena.rdf.model.Property Property() {
			// TODO Auto-generated method stub
			return null;
		}

	},
	MessageContent() {
		@Override
		public String IRI() {
			return msm + this.name();
		}

		@Override
		public Resource Node() {
			Model model = ModelFactory.createDefaultModel();
			return model.createResource(msm + this.name());
		}

		@Override
		public com.hp.hpl.jena.rdf.model.Property Property() {
			// TODO Auto-generated method stub
			return null;
		}
	},

	hasOperation() {

		@Override
		public String IRI() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Resource Node() {
			Model model = ModelFactory.createDefaultModel();
			return model.createResource(msm + this.name());
		}

		@Override
		public com.hp.hpl.jena.rdf.model.Property Property() {
			Model model = ModelFactory.createDefaultModel();
			return model.createProperty(msm + this.name());
		}

	},

	hasInput() {

		@Override
		public String IRI() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Resource Node() {
			Model model = ModelFactory.createDefaultModel();
			return model.createResource(msm + this.name());
		}

		@Override
		public com.hp.hpl.jena.rdf.model.Property Property() {
			Model model = ModelFactory.createDefaultModel();
			return model.createProperty(msm + this.name());
		}

	},
	hasOutput() {

		@Override
		public String IRI() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Resource Node() {
			Model model = ModelFactory.createDefaultModel();
			return model.createResource(msm + this.name());
		}

		@Override
		public com.hp.hpl.jena.rdf.model.Property Property() {
			Model model = ModelFactory.createDefaultModel();
			return model.createProperty(msm + this.name());
		}

	},
	hasMandatoryPart() {

		@Override
		public String IRI() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Resource Node() {
			Model model = ModelFactory.createDefaultModel();
			return model.createResource(msm + this.name());
		}

		@Override
		public com.hp.hpl.jena.rdf.model.Property Property() {
			Model model = ModelFactory.createDefaultModel();
			return model.createProperty(msm + this.name());
		}

	},
	hasOptionalPart() {

		@Override
		public String IRI() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Resource Node() {
			Model model = ModelFactory.createDefaultModel();
			return model.createResource(msm + this.name());
		}

		@Override
		public com.hp.hpl.jena.rdf.model.Property Property() {
			Model model = ModelFactory.createDefaultModel();
			return model.createProperty(msm + this.name());
		}

	},
	modelReference() {

		@Override
		public String IRI() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Resource Node() {
			Model model = ModelFactory.createDefaultModel();
			return model.createResource(sawsdl + this.name());
		}

		@Override
		public com.hp.hpl.jena.rdf.model.Property Property() {
			Model model = ModelFactory.createDefaultModel();
			return model.createProperty(sawsdl + this.name());
		}

	},
	hasValue() {

		@Override
		public String IRI() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Resource Node() {
			Model model = ModelFactory.createDefaultModel();
			return model.createResource(twa + this.name());
		}

		@Override
		public com.hp.hpl.jena.rdf.model.Property Property() {
			Model model = ModelFactory.createDefaultModel();
			return model.createProperty(twa + this.name());
		}

	}
	,
	hasDatatypeValue() {

		@Override
		public String IRI() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Resource Node() {
			Model model = ModelFactory.createDefaultModel();
			return model.createResource(twa + this.name());
		}

		@Override
		public com.hp.hpl.jena.rdf.model.Property Property() {
			Model model = ModelFactory.createDefaultModel();
			return model.createProperty(twa + this.name());
		}

	}

	;

	public abstract String IRI();
	public abstract Resource Node();
	public abstract com.hp.hpl.jena.rdf.model.Property Property();

	private MSM() {

	}

	private static final String msm = "http://iserve.kmi.open.ac.uk/ns/msm#";
	private static final String sawsdl = "http://www.w3.org/ns/sawsdl#";
	private static final String twa = "http://www.theworldavatar.com#";

}