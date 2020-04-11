class Expert < ApplicationRecord
  has_many :exp_procedures
  has_many :procedures, through: :exp_procedures, dependent: :destroy

  def as_json(options={})
   super(options.merge methods: [:expertProcedures])
  end

  def expertProcedures
    exp_procedures
  end

  def expProceduresJson
    exp_procedures.as_json
  end

end
